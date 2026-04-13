import cv2
import numpy as np
from fastapi import UploadFile
import pytesseract

from src.utils.GenAi import ExpenseLLMService
from src.io.response.ocr_image_response import OcrImageResponse
from uuid import UUID
from datetime import date


valid_extensions = (".jpg", ".jpeg", ".png")

pytesseract.pytesseract.tesseract_cmd = r"C:\Users\himan\AppData\Local\Programs\Tesseract-OCR\tesseract.exe"
       


class ImageOcrService:

    def __init__(self):
        self.llm_service = ExpenseLLMService()

    # (keep your existing private methods SAME)
    # 🔒 PRIVATE: Image Cleaning
    def _image_cleaning(self, image: UploadFile) -> np.ndarray:
        try:
            # 1. Validate file type
            if not image.filename.lower().endswith(valid_extensions):
                raise ValueError("Invalid file type. Only JPG, JPEG, PNG allowed.")

            # 2. Read file bytes
            contents = image.file.read()

            # Reset pointer (important if reused)
            image.file.seek(0)

            # 3. Convert to numpy array
            nparr = np.frombuffer(contents, np.uint8)

            # 4. Decode image
            color_image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

            if color_image is None:
                raise ValueError("Invalid image data")

            # 5. Preprocessing pipeline
            grey_image = cv2.cvtColor(color_image, cv2.COLOR_BGR2GRAY)
            blur_image = cv2.GaussianBlur(grey_image, (5, 5), 0)

            _, binary_image = cv2.threshold(
                blur_image, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU
            )

            return binary_image

        except Exception as e:
            raise Exception(f"Image cleaning failed: {str(e)}")
        
    # 🔒 PRIVATE: OCR Extraction
    def _perform_ocr(self, image: np.ndarray) -> str:
        try:
            # Extract text using pytesseract
            text = pytesseract.image_to_string(image)

            # Optional cleanup
            text = text.strip()

            return text

        except Exception as e:
            raise Exception(f"OCR failed: {str(e)}")

    # 🌐 PUBLIC: Pipeline Method
    def extract_text(self, image: UploadFile) -> str:
        try:
            # Step 1: Clean image
            cleaned_image = self._image_cleaning(image)

            # Step 2: Extract text
            text = self._perform_ocr(cleaned_image)

            return text

        except Exception as e:
            return str(e)

    def extract_text(self, image: UploadFile) -> str:
        cleaned_image = self._image_cleaning(image)
        return self._perform_ocr(cleaned_image)

    # ✅ NEW METHOD
    def process(self, image: UploadFile, user_id: UUID) -> OcrImageResponse:
        try:
            # Step 1: OCR
            text = self.extract_text(image)

            # Step 2: LLM extraction
            expense_data = self.llm_service.extract(text)

            return OcrImageResponse(
                userId=user_id,
                expenseData=expense_data,
                created_at=date.today()
            )

        except Exception as e:
            raise Exception(f"Image OCR Pipeline failed: {str(e)}")