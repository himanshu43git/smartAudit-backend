from typing import List
import os
from dotenv import load_dotenv

from langchain_groq import ChatGroq
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

from src.model.expense_data import ExpenseData


# ✅ Load environment variables
load_dotenv(dotenv_path=".env")


class ExpenseLLMService:

    def __init__(self):
        groq_api_key = os.getenv("GROQ_API_KEY")

        if not groq_api_key:
            raise ValueError("GROQ_API_KEY not found in environment variables")

        self.llm = ChatGroq(
            model="llama-3.3-70b-versatile",
            temperature=0,
            groq_api_key=groq_api_key   # ✅ explicitly set
        )

        self.parser = JsonOutputParser(pydantic_object=ExpenseData)

        self.prompt = ChatPromptTemplate.from_messages([
            ("system", self._system_prompt()),
            ("human", self._human_prompt())
        ])

        self.chain = self.prompt | self.llm | self.parser

    def _system_prompt(self) -> str:
        return """
You are an intelligent invoice/receipt parser.

Extract structured expense data.

RULES:
- Output MUST be valid JSON
- No explanations
- Missing values → null
- Extract multiple items if present
- Amounts must be numeric
- Currency should be like INR, USD etc.
- Date should be ISO format if possible

FIELDS:
- vendor_name → shop/vendor name
- date → transaction date
- total_amount → total bill amount
- currency → INR, USD etc.
- items → list of purchased items

Each item should have:
- name
- quantity
- price
- total
"""

    def _human_prompt(self) -> str:
        return """
Extract expense data from:

{text}

{format_instructions}
"""

    def extract(self, text: str) -> List[ExpenseData]:
        result = self.chain.invoke({
            "text": text,
            "format_instructions": self.parser.get_format_instructions()
        })

        if isinstance(result, list):
            return result
        else:
            return [result]