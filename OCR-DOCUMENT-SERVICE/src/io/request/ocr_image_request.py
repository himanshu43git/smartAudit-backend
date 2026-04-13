from uuid import UUID
from fastapi import File, Form, UploadFile
from pydantic import BaseModel


class OcrImageRequest(BaseModel):
    def __init__(
        self,
        userId: UUID = Form(...),
        image: UploadFile = File(...)
    ):
        self.userId = userId
        self.image = image