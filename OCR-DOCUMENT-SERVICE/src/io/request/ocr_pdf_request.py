from uuid import UUID
from fastapi import File, Form, UploadFile


class OcrPDFRequest:
    def __init__(
        self,
        userId: UUID = Form(...),  # Form() tells FastAPI this is a multipart field
        pdf_file: UploadFile = File(...) # File() tells FastAPI this is the upload stream
    ):
        self.userId = userId
        self.pdf_file = pdf_file
        
    # userId: UUID = Form(...),  # Form() tells FastAPI this is a multipart field
    # pdf_file: UploadFile = File(...) # File() tells FastAPI this is the upload stream