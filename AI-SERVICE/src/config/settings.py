# src/core/settings.py
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    # AI (Groq) Configuration
    AI_MODEL_NAME: str
    AI_API_KEY: str

    # Tells Pydantic to automatically load these from the .env file in the root directory
    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8")

# We instantiate it here so you can just import `settings` directly into your other files
settings = Settings()