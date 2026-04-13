# from pydantic import BaseModel

# class ExpenseData(BaseModel):
#     vendor_name: str = ""
#     date: str = ""
#     total_amount: float = 0.0
#     currency: str = ""
#     items: list = []



from pydantic import BaseModel, Field
from typing import List, Optional


class ExpenseItem(BaseModel):
    name: Optional[str] = Field(default=None, description="Item name")
    quantity: Optional[float] = Field(default=None, description="Quantity")
    price: Optional[float] = Field(default=None, description="Item price")
    total: Optional[float] = Field(default=None, description="Total for item")


class ExpenseData(BaseModel):
    vendor_name: Optional[str] = None
    date: Optional[str] = None  # ISO string
    total_amount: Optional[float] = None
    currency: Optional[str] = None
    items: Optional[List[ExpenseItem]] = None