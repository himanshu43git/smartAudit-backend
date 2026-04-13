from uuid import UUID
from datetime import date
from src.model.expense_data import ExpenseData
from pydantic import BaseModel


class OcrPDFResponse(BaseModel):
    userId: UUID
    expenseData: list[ExpenseData]
    created_at: date