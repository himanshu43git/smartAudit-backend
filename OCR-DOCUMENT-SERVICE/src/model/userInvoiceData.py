from beanie import Document
from pydantic import Field
from uuid import UUID, uuid4

class Invoice(Document):
    id: UUID = Field(default_factory=uuid4)   # Primary key
    userId: UUID
    invoice_url: str

    class Settings:
        name = "invoices"   # collection name