from pathlib import Path
from typing import List
from datetime import date
from uuid import UUID

from langchain_community.document_loaders import PyMuPDFLoader, Docx2txtLoader
from langchain_core.documents import Document

from src.utils.GenAi import ExpenseLLMService
from src.io.response.ocr_pdf_response import OcrPDFResponse


class PdfOCRService:

    def __init__(self):
        self.llm_service = ExpenseLLMService()

    # -------- STEP 1: Validate file --------
    def _validate_file(self, file_path: str) -> str:
        path = Path(file_path)

        if not path.exists():
            raise FileNotFoundError(f"File not found: {file_path}")

        ext = path.suffix.lower()

        if ext not in [".pdf", ".docx"]:
            raise ValueError("Only .pdf and .docx files are allowed")

        return ext

    # -------- STEP 2: Load document --------
    def _load_document(self, file_path: str, ext: str) -> List[Document]:
        if ext == ".pdf":
            loader = PyMuPDFLoader(file_path)
        elif ext == ".docx":
            loader = Docx2txtLoader(file_path)
        else:
            raise ValueError("Unsupported file type")

        return loader.load()

    # -------- STEP 3: Extract text --------
    def _extract_text(self, documents: List[Document]) -> str:
        return "\n\n".join(doc.page_content for doc in documents)

    # -------- FINAL PIPELINE --------
    def process(self, file_path: str, user_id: UUID) -> OcrPDFResponse:
        try:
            # Step 1: Validate
            ext = self._validate_file(file_path)

            # Step 2: Load
            documents = self._load_document(file_path, ext)

            # Step 3: Extract text
            text = self._extract_text(documents)

            # Step 4: LLM extraction
            expense_data = self.llm_service.extract(text)

            # Step 5: Build response
            return OcrPDFResponse(
                userId=user_id,
                expenseData=expense_data,
                created_at=date.today()
            )

        except Exception as e:
            raise Exception(f"PDF OCR Pipeline failed: {str(e)}")