from fastapi import FastAPI
from uuid import UUID

from src.services.AnalysisService import AnalysisService

app = FastAPI()

analysis_service = AnalysisService()

@app.get("/analyze-expense/{user_id}/{days}")
async def analyze_expense_endpoint(user_id: UUID, days: int):
    # This will now reach out to localhost:13004 and grab the data
    data = await analysis_service.analyze_expense(user_id, days)
    
    return {"user_id": str(user_id), "fetched_data": data}



@app.get("/")
async def test():
    return {"message": "Hello from ai-service!"}

def main():
    print("Hello from ai-service!")


if __name__ == "__main__":
    main()
