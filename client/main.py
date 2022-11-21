from fastapi import FastAPI
from routers import test
from routers import free_with_sub

app = FastAPI()
app.include_router(test.router)
app.include_router(free_with_sub.router)


@app.get("/")
async def root():
    return {"message": "Hello World"}
