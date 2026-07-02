# from uuid import UUID
# from fastapi import File, Form, UploadFile
# from pydantic import BaseModel


# class OcrImageRequest(BaseModel):
#     def __init__(
#         self,
#         userId: UUID = Form(...),
#         image: UploadFile = File(...)
#     ):
#         self.userId = userId
#         self.image = image

from uuid import UUID
from fastapi import Form, File, UploadFile
from pydantic import BaseModel

class OcrImageRequest(BaseModel):
    userId: UUID
    image: UploadFile

    @classmethod
    def as_form(
        cls,
        userId: UUID = Form(...),
        image: UploadFile = File(...)
    ):
        return cls(
            userId=userId,
            image=image
        )