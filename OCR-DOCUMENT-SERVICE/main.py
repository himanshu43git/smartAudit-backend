from fastapi import FastAPI, Depends
import os
import tempfile

from src.io.request.ocr_image_request import OcrImageRequest
from src.io.request.ocr_pdf_request import OcrPDFRequest
from src.io.response.ocr_image_response import OcrImageResponse
from src.services.imageOcrService.image_service import ImageOcrService
from src.services.pdfOcrService.pdf_service import PdfOCRService
from src.io.response.ocr_pdf_response import OcrPDFResponse
from src.io.request.ocr_image_request import OcrImageRequest



app = FastAPI()


@app.get("/")
def read_root() -> str:
    return "Hello World"

@app.post("/ocr-img-document", response_model=OcrImageResponse)
async def ocr_image(
    img_file: OcrImageRequest = Depends(OcrImageRequest.as_form)
) -> OcrImageResponse:

    ocr_service = ImageOcrService()

    # ✅ FULL PIPELINE (OCR + LLM)
    result = ocr_service.process(
        image=img_file.image,
        user_id=img_file.userId
    )

    return result

@app.post("/ocr-pdf-document", response_model=OcrPDFResponse)
async def ocr_pdf(
    pdf_file: OcrPDFRequest = Depends()
) -> OcrPDFResponse:

    service = PdfOCRService()

    suffix = os.path.splitext(pdf_file.pdf_file.filename)[1]

    # ✅ Create temp file (required for PyMuPDF)
    with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as temp_file:
        content = await pdf_file.pdf_file.read()
        temp_file.write(content)
        temp_path = temp_file.name

    try:
        # ✅ FULL PIPELINE (PDF → TEXT → LLM)
        result = service.process(
            file_path=temp_path,
            user_id=pdf_file.userId
        )

        return result

    finally:
        # ✅ Clean up
        os.remove(temp_path)


def main():
    print("Hello from ocr-document-service!")


if __name__ == "__main__":
    main()

