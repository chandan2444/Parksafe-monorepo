# Frontend

This is a plain HTML/CSS/JavaScript frontend. No npm installation is required.

## Run
Open `index.html` in a browser after all three Spring Boot services are running.
The app calls:
- Parking Service: http://localhost:8081
- Entry/Exit Service: http://localhost:8082
- Session Service: http://localhost:8083

For a local static server (recommended), from this folder run:
```bash
python -m http.server 5500
```
Then open http://localhost:5500.

The backend controllers enable CORS for this learning demo. Restrict origins before production use.
