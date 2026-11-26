from pathlib import Path
from fastapi import FastAPI
from fastapi.responses import FileResponse
from fastapi.staticfiles import StaticFiles

from routers.funcionario_router import router as funcionario_router
from routers.auth_router import router as auth_router
from routers.relatorios_router import router as relatorios_router

BASE_DIR = Path(__file__).resolve().parent
STATIC_DIR = BASE_DIR / "static"

app = FastAPI(
    title="HemoBank API",
    description="API de gerenciamento do sistema HemoBank",
    version="1.2.0",
)

app.mount("/static", StaticFiles(directory=STATIC_DIR), name="static")

@app.get("/", include_in_schema=False)
def interface():
    return FileResponse(STATIC_DIR / "index.html")

@app.get("/status")
def verificar_status():
    return {"sistema": "HemoBank", "status": "online"}

app.include_router(auth_router)
app.include_router(funcionario_router)

app.include_router(relatorios_router)
