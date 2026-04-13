# src/services/ai.py
import json
from groq import AsyncGroq
from src.config.settings import settings


class AiService:
    def __init__(self):
        self.model_name = settings.AI_MODEL_NAME
        self.client = AsyncGroq(api_key=settings.AI_API_KEY)

    async def analyze_expenses_batch(self, expenses_data: list[dict]):
        """Analyzes a list of expenses and returns aggregated insights."""

        if not expenses_data:
            return {
                "summary": {},
                "transactions_analysis": [],
                "insights": ["No transactions provided."],
                "recommendation": "No analysis performed."
            }

        system_prompt = """
You are an expert AI financial analyst powering an expense tracker application.

You will receive a JSON array of financial transactions.

Your job is to:
1. Aggregate totals (separating expenses vs income).
2. Validate/refine categorization.
3. Detect anomalies, fraud risks, and inconsistencies.
4. Generate EXACTLY 5 to 6 prioritized insights.

Return STRICT JSON:

{
  "summary": {
    "total_transactions": int,
    "total_expense_amount": float,
    "total_income_amount": float,
    "high_risk_count": int,
    "categories_breakdown": {
      "<category>": float
    }
  },
  "transactions_analysis": [
    {
      "id": string,
      "refined_category": string,
      "is_expense": boolean,
      "fraud_score": float,
      "priority": "low" | "medium" | "high" | "critical",
      "flags": [string]
    }
  ],
  "insights": [
    "Insight 1",
    "Insight 2",
    "Insight 3",
    "Insight 4",
    "Insight 5",
    "Insight 6"
  ],
  "recommendation": "final action summary"
}

Rules:
- Salary/income → is_expense = false
- Detect missing receipts, pending payments, high amounts
- Use merchantName, description, title for categorization
- Output ONLY JSON
"""

        user_prompt = f"""
Analyze this dataset:

{json.dumps(expenses_data)}
"""

        try:
            chat_completion = await self.client.chat.completions.create(
                messages=[
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": user_prompt}
                ],
                model=self.model_name,
                temperature=0.0,
                response_format={"type": "json_object"},
            )

            result = json.loads(chat_completion.choices[0].message.content)

            # ---- Safety defaults ----
            result.setdefault("summary", {})
            result.setdefault("transactions_analysis", [])
            result.setdefault("insights", [])
            result.setdefault("recommendation", "")

            return result

        except Exception as e:
            print(f"Groq API Error: {e}")

            return {
                "summary": {
                    "total_transactions": len(expenses_data),
                    "total_expense_amount": 0.0,
                    "total_income_amount": 0.0,
                    "high_risk_count": 0,
                    "categories_breakdown": {}
                },
                "transactions_analysis": [],
                "insights": [
                    "AI analysis failed.",
                    "Unable to compute totals.",
                    "Unable to detect anomalies.",
                    "Manual verification required.",
                    "System fallback triggered."
                ],
                "recommendation": "Manual review required."
            }