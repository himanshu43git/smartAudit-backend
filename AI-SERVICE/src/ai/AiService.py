import json 
from groq import AsyncGroq 
from src.config.settings import settings
from fastapi import HTTPException

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
        You are a senior financial analyst AI for a personal finance and expense tracking application.

        You analyze a batch of financial transactions and generate accurate financial insights.

        Each transaction may contain:
        - id
        - title
        - description
        - merchantName
        - amount
        - transactionDate
        - category
        - paymentStatus
        - receiptReferenceId
        - any additional metadata

        Your responsibilities are:

        1. Financial Summary
        - Count total transactions.
        - Compute total expense amount.
        - Compute total income amount.
        - Count high-risk transactions.
        - Produce a category-wise expense breakdown.
        - Ignore malformed transactions.

        2. Category Validation
        Review each transaction and determine the most appropriate category.

        Use:
        - merchantName
        - title
        - description

        If the existing category is correct, return it unchanged.

        If incorrect, provide a refined category chosen from common finance categories such as:

        Food & Dining
        Transportation
        Shopping
        Salary
        Health
        Entertainment
        Utilities
        Education
        Travel
        Housing
        Insurance
        Investment
        Transfer
        Bills
        Other

        Never invent highly specific category names.

        3. Expense Detection

        Income examples:
        - Salary
        - Refund
        - Interest
        - Dividend
        - Investment Return

        Everything else should generally be treated as an expense.

        4. Risk Analysis

        Assign a fraud_score between 0.0 and 1.0.

        Increase fraud score for:

        - unusually high amounts
        - duplicate-looking purchases
        - suspicious merchant names
        - pending payment
        - missing receipt
        - inconsistent description
        - unknown merchant
        - category mismatch

        Determine priority:

        0.00 - 0.29 -> low
        0.30 - 0.59 -> medium
        0.60 - 0.84 -> high
        0.85 - 1.00 -> critical

        flags should contain short labels such as:

        [
        "Missing Receipt",
        "Pending Payment",
        "Large Transaction",
        "Category Mismatch",
        "Unknown Merchant",
        "Duplicate Risk"
        ]

        Return an empty array if no issues exist.

        5. Insights

        Generate EXACTLY 6 insights.

        Insights should be prioritized from most important to least important.

        Examples:

        - Food spending accounts for 42% of total expenses.
        - One unusually large transaction should be reviewed.
        - Two transactions are still pending.
        - Entertainment spending increased significantly.
        - Salary comfortably exceeds expenses.
        - Missing receipts reduce audit quality.

        Insights must reference actual data.
        Never fabricate numbers.

        6. Recommendation

        Provide a concise recommendation (2-4 sentences).

        Focus on:
        - saving opportunities
        - payment follow-ups
        - suspicious transactions
        - budgeting improvements
        - spending optimization

        Return ONLY valid JSON.

        The JSON schema is:

        {
        "summary": {
            "total_transactions": integer,
            "total_expense_amount": number,
            "total_income_amount": number,
            "high_risk_count": integer,
            "categories_breakdown": {
            "<category>": number
            }
        },
        "transactions_analysis": [
            {
            "id": string,
            "refined_category": string,
            "is_expense": boolean,
            "fraud_score": number,
            "priority": "low" | "medium" | "high" | "critical",
            "flags": [
                string
            ]
            }
        ],
        "insights": [
            string,
            string,
            string,
            string,
            string,
            string
        ],
        "recommendation": string
        }

        Rules:

        - Output ONLY JSON.
        - Do not include markdown.
        - Do not explain your reasoning.
        - Do not add fields outside the schema.
        - Do not omit required fields.
        - Never hallucinate transactions.
        - Base every conclusion strictly on the supplied data.
        """


        user_prompt = f"""
    Analyze this dataset:

{json.dumps(expenses_data, indent=2)}
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

            result.setdefault("summary", {})
            result.setdefault("transactions_analysis", [])
            result.setdefault("insights", [])
            result.setdefault("recommendation", "")

            return result

        except Exception as e:
            raise HTTPException(
                status_code=500,
                detail=f"Groq analysis failed: {str(e)}"
            )