const API_URL = "http://localhost:8081/notes";

document.addEventListener("DOMContentLoaded", fetchNotes);

async function fetchNotes() {
    try {
        const response = await fetch(API_URL);
        const notes = await response.json();
        displayNotes(notes);
    } catch (error) {
        console.error("Error fetching notes:", error);
    }
}

function displayNotes(notes) {
    const container = document.getElementById("notesContainer");
    container.innerHTML = "";

    notes.forEach(note => {
        container.innerHTML += `
            <div class="col-md-4">
                <div class="note-card shadow-sm">
                    <h5 class="note-title">${note.title}</h5>
                    <p class="note-desc">${note.description}</p>

                    <div class="note-actions">
                        <button class="btn btn-sm btn-outline-dark"
                            onclick="editNote(${note.id}, \`${note.title}\`, \`${note.description}\`)">
                            Edit
                        </button>

                        <button class="btn btn-sm btn-outline-danger"
                            onclick="deleteNote(${note.id})">
                            Delete
                        </button>
                    </div>
                </div>
            </div>
        `;
    });
}

async function saveNote() {
    const id = document.getElementById("noteId").value;
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();

    if (!title || !description) {
        alert("Please fill all fields");
        return;
    }

    const note = { title, description };

    try {
        if (id) {
            // UPDATE
            await fetch(`${API_URL}/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(note)
            });
        } else {
            // CREATE
            await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(note)
            });
        }

        clearForm();
        fetchNotes();

    } catch (error) {
        console.error("Error saving note:", error);
    }
}

function editNote(id, title, description) {
    document.getElementById("noteId").value = id;
    document.getElementById("title").value = title;
    document.getElementById("description").value = description;
}

async function deleteNote(id) {
    try {
        await fetch(`${API_URL}/delete/${id}`, {
            method: "PUT"
        });

        fetchNotes();

    } catch (error) {
        console.error("Error deleting note:", error);
    }
}

function clearForm() {
    document.getElementById("noteId").value = "";
    document.getElementById("title").value = "";
    document.getElementById("description").value = "";
}