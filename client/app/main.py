from fastapi import FastAPI
from app.routers import sources, test, clients, users, search, auth
from .constants import CLIENT_DESCRIPTION, TAG_METADATA

app = FastAPI(
    title="OASS Client",
    description=CLIENT_DESCRIPTION,
    openapi_tags=TAG_METADATA
)

# Add all the different routers
app.include_router(test.router)
app.include_router(sources.router)
app.include_router(clients.router)
app.include_router(users.router)
app.include_router(search.router)
app.include_router(auth.router)


@app.get("/")
async def root():
    return {"message": "Hello World"}
