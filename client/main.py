from fastapi import FastAPI
from routers import sources, test
from constants import CLIENT_DESCRIPTION,TAG_METADATA

app = FastAPI(
    title="OASS Client",
    description=CLIENT_DESCRIPTION,
    openapi_tags=TAG_METADATA
)
app.include_router(test.router)
app.include_router(sources.router)


@app.get("/")
async def root():
    return {"message": "Hello World"}
