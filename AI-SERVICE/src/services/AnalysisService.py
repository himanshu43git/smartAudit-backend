# src/services/AnalysisService.py
from uuid import UUID
import httpx
from fastapi import HTTPException

from src.ai.AiService import AiService


class AnalysisService:
    def __init__(self):
        self.user_expense_url = "http://localhost:13004/expense/getExpenses/{user_id}?days={days}"
        self.ai_service = AiService()

    async def analyze_expense(self, user_id: UUID, days: int):
        url = self.user_expense_url.format(user_id=str(user_id), days = days)

        async with httpx.AsyncClient(timeout=10.0) as client:
            try:
                response = await client.get(url)
                response.raise_for_status()

                expense_data = response.json()

                # Normalize response (important)
                if isinstance(expense_data, dict) and "data" in expense_data:
                    expense_data = expense_data["data"]

                if not expense_data:
                    raise HTTPException(
                        status_code=404,
                        detail="No expenses found for this user"
                    )

                # Call AI service
                analysis_result = await self.ai_service.analyze_expenses_batch(
                    expenses_data=expense_data
                )

                return analysis_result

            except httpx.HTTPStatusError as e:
                raise HTTPException(
                    status_code=e.response.status_code,
                    detail=f"Failed to fetch expenses: {e.response.text}"
                )

            except httpx.RequestError:
                raise HTTPException(
                    status_code=503,
                    detail="Expense service unavailable"
                )