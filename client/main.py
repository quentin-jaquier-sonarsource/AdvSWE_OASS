from fastapi import FastAPI
from routers import test, free_with_sub
from constants import CLIENT_DESCRIPTION,TAG_METADATA

app = FastAPI(
    title="OASS Client",
    description=CLIENT_DESCRIPTION,
    openapi_tags=TAG_METADATA
)
app.include_router(test.router)
app.include_router(free_with_sub.router)


@app.get("/")
async def root():
    return {"message": "Hello World"}
