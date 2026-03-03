const generateBtn = document.getElementById("generateBtn");
const emailContent = document.getElementById("emailContent");
const tone = document.getElementById("tone");
const responseBox = document.getElementById("responseBox");
const loading = document.getElementById("loading");
const errorBox = document.getElementById("errorBox");

generateBtn.addEventListener("click", async () => {

    const emailText = emailContent.value.trim();

    if (!emailText) {
        showError("Please enter email content.");
        return;
    }

    clearUI();
    loading.classList.remove("hidden");

    try {
        const response = await fetch("/api/v1/emails/generate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                emailContent: emailText,
                tone: tone.value
            })
        });

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        const data = await response.json();

        if(!data.data || !data.data.reply){
            throw new Error("Invalid response structure");
        }

        responseBox.textContent = data.data.reply;

    } catch (err) {
        showError("Failed to generate reply. " + err.message);
    } finally {
        loading.classList.add("hidden");
    }
});

function showError(message) {
    errorBox.textContent = message;
    errorBox.classList.remove("hidden");
}

function clearUI() {
    errorBox.classList.add("hidden");
    errorBox.textContent = "";
    responseBox.textContent = "";
}