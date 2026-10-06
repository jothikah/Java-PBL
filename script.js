// ==========================================
// FLASHCARD LEARNING SYSTEM
// JavaScript Functionality
// ==========================================


// ---------- Flashcard Data ----------

const flashcards = [

    {
        category: "Java",
        question: "What is JVM in Java?",
        answer: "JVM stands for Java Virtual Machine. It executes Java bytecode.",
        hint: "It executes Java bytecode.",
        difficulty: "EASY"
    },

    {
        category: "Java",
        question: "What is JDK?",
        answer: "JDK stands for Java Development Kit. It is used to develop Java applications.",
        hint: "It contains tools needed to develop Java programs.",
        difficulty: "EASY"
    },

    {
        category: "OOP",
        question: "What is encapsulation?",
        answer: "Encapsulation is the process of wrapping data and methods into a single unit such as a class.",
        hint: "Think about data hiding.",
        difficulty: "EASY"
    },

    {
        category: "OOP",
        question: "What is polymorphism?",
        answer: "Polymorphism allows one interface or method to represent different forms of behaviour.",
        hint: "One name, many forms.",
        difficulty: "MEDIUM"
    },

    {
        category: "Strings",
        question: "Is String mutable in Java?",
        answer: "No. String objects are immutable in Java.",
        hint: "Can the original String object be changed?",
        difficulty: "EASY"
    },

    {
        category: "Strings",
        question: "What is StringBuilder?",
        answer: "StringBuilder is a mutable sequence of characters used for efficient string manipulation.",
        hint: "It can modify strings.",
        difficulty: "MEDIUM"
    },

    {
        category: "Inheritance",
        question: "What is inheritance?",
        answer: "Inheritance allows a class to acquire properties and methods from another class.",
        hint: "A child class gets features from a parent class.",
        difficulty: "EASY"
    },

    {
        category: "Inheritance",
        question: "Which keyword is used for class inheritance in Java?",
        answer: "The extends keyword is used for class inheritance.",
        hint: "Example: class Dog ___ Animal",
        difficulty: "EASY"
    }

];


// ---------- Variables ----------

let currentCardIndex = 0;

let currentCategory = "All";

let score = 0;

let xp = 0;

let streak = 0;

let bestStreak = 0;

let answeredCards = 0;

let favorites = [];

let showingAnswer = false;


// ---------- DOM Elements ----------

const usernameInput =
    document.getElementById("username");

const startButton =
    document.querySelector(".start-btn");

const continueButton =
    document.querySelector(".primary-btn");

const flashcard =
    document.querySelector(".flashcard");

const flashcardQuestion =
    flashcard.querySelector("h3");

const hintText =
    flashcard.querySelector(".hint");

const difficultyText =
    flashcard.querySelector(".difficulty");

const correctButton =
    document.querySelector(".correct-btn");

const wrongButton =
    document.querySelector(".wrong-btn");


// ---------- Start Learning ----------

startButton.addEventListener("click", function () {

    document.getElementById("categories")
        .scrollIntoView({
            behavior: "smooth"
        });

});


// ---------- Continue Button ----------

continueButton.addEventListener("click", function () {

    const name = usernameInput.value.trim();

    if (name === "") {

        alert("Please enter your name.");

        usernameInput.focus();

        return;
    }

    localStorage.setItem(
        "flashcardUsername",
        name
    );

    alert(
        "Welcome " + name + "! Let's start learning."
    );

    document.getElementById("categories")
        .scrollIntoView({
            behavior: "smooth"
        });

});


// ---------- Category Buttons ----------

const categoryButtons =
    document.querySelectorAll(".category-card button");

const categoryCards =
    document.querySelectorAll(".category-card");


categoryButtons.forEach(function (button, index) {

    button.addEventListener("click", function () {

        currentCategory =
            categoryCards[index]
                .querySelector("h3")
                .textContent;

        currentCardIndex = 0;

        score = 0;

        xp = 0;

        streak = 0;

        answeredCards = 0;

        showingAnswer = false;

        loadCategoryCards();

        loadFlashcard();

        document.querySelector(".flashcard-section")
            .scrollIntoView({
                behavior: "smooth"
            });

    });

});


// ---------- Get Selected Category Cards ----------

function getCategoryCards() {

    if (currentCategory === "All") {

        return flashcards;

    }

    return flashcards.filter(function (card) {

        return card.category === currentCategory;

    });

}


// ---------- Load Category ----------

function loadCategoryCards() {

    const cards =
        getCategoryCards();

    if (cards.length === 0) {

        alert("No flashcards available.");

        return;
    }

    currentCardIndex =
        Math.floor(
            Math.random() * cards.length
        );

}


// ---------- Load Flashcard ----------

function loadFlashcard() {

    const cards =
        getCategoryCards();

    if (cards.length === 0) {
        return;
    }

    const card =
        cards[currentCardIndex];

    flashcardQuestion.textContent =
        card.question;

    hintText.textContent =
        "Hint: " + card.hint;

    difficultyText.textContent =
        card.difficulty;

    showingAnswer = false;

    correctButton.textContent =
        "I Know";

    wrongButton.textContent =
        "Need Practice";

    updateDifficultyStyle();

}


// ---------- Difficulty ----------

function updateDifficultyStyle() {

    const difficulty =
        difficultyText.textContent;

    if (difficulty === "EASY") {

        difficultyText.style.background =
            "#dcfce7";

        difficultyText.style.color =
            "#15803d";

    }
    else if (difficulty === "MEDIUM") {

        difficultyText.style.background =
            "#fef3c7";

        difficultyText.style.color =
            "#b45309";

    }
    else {

        difficultyText.style.background =
            "#fee2e2";

        difficultyText.style.color =
            "#dc2626";
    }

}


// ---------- Show Answer ----------

function showAnswer() {

    const cards =
        getCategoryCards();

    if (cards.length === 0) {
        return;
    }

    const card =
        cards[currentCardIndex];

    if (!showingAnswer) {

        flashcardQuestion.textContent =
            card.answer;

        hintText.textContent =
            "Question: " + card.question;

        correctButton.textContent =
            "I Got It";

        wrongButton.textContent =
            "I Didn't Know";

        showingAnswer = true;

    }

}


// ---------- Correct Answer ----------

correctButton.addEventListener(
    "click",
    function () {

        if (!showingAnswer) {

            showAnswer();

            return;
        }

        score++;

        xp += 10;

        streak++;

        answeredCards++;

        if (streak > bestStreak) {

            bestStreak = streak;

        }

        updateProgress();

        showMessage(
            "Correct! +10 XP"
        );

        nextCard();

    }
);


// ---------- Wrong Answer ----------

wrongButton.addEventListener(
    "click",
    function () {

        if (!showingAnswer) {

            showAnswer();

            return;
        }

        streak = 0;

        answeredCards++;

        updateProgress();

        showMessage(
            "Keep practicing!"
        );

        nextCard();

    }
);


// ---------- Next Card ----------

function nextCard() {

    const cards =
        getCategoryCards();

    if (cards.length === 0) {
        return;
    }

    setTimeout(function () {

        currentCardIndex++;

        if (
            currentCardIndex >=
            cards.length
        ) {

            currentCardIndex = 0;

        }

        loadFlashcard();

    }, 700);

}


// ---------- Update Progress ----------

function updateProgress() {

    const total =
        getCategoryCards().length;

    let percentage = 0;

    if (total > 0) {

        percentage =
            Math.round(
                (answeredCards / total) * 100
            );

    }

    if (percentage > 100) {

        percentage = 100;

    }

    const statCards =
        document.querySelectorAll(".stat-card");

    if (statCards.length >= 4) {

        statCards[0]
            .querySelector("h3")
            .textContent = score;

        statCards[1]
            .querySelector("h3")
            .textContent =
            xp + " XP";

        statCards[2]
            .querySelector("h3")
            .textContent =
            bestStreak + " 🔥";

        statCards[3]
            .querySelector("h3")
            .textContent =
            percentage + "%";

    }

    const progressFill =
        document.querySelector(".progress-fill");

    if (progressFill) {

        progressFill.style.width =
            percentage + "%";

    }

    const progressText =
        document.querySelector(
            ".progress-bar-container p"
        );

    if (progressText) {

        progressText.textContent =
            percentage + "% Completed";

    }

}


// ---------- Message ----------

function showMessage(message) {

    const messageBox =
        document.createElement("div");

    messageBox.textContent =
        message;

    messageBox.style.position =
        "fixed";

    messageBox.style.top =
        "90px";

    messageBox.style.right =
        "25px";

    messageBox.style.padding =
        "15px 25px";

    messageBox.style.background =
        "#111827";

    messageBox.style.color =
        "white";

    messageBox.style.borderRadius =
        "10px";

    messageBox.style.zIndex =
        "1000";

    document.body.appendChild(
        messageBox
    );

    setTimeout(function () {

        messageBox.remove();

    }, 1500);

}


// ---------- Favorite System ----------

function addFavorite() {

    const cards =
        getCategoryCards();

    if (cards.length === 0) {
        return;
    }

    const card =
        cards[currentCardIndex];

    const alreadyFavorite =
        favorites.some(function (favorite) {

            return favorite.question ===
                card.question;

        });

    if (!alreadyFavorite) {

        favorites.push(card);

        localStorage.setItem(
            "flashcardFavorites",
            JSON.stringify(favorites)
        );

        showMessage(
            "Added to Favorites!"
        );

    }
    else {

        showMessage(
            "Already in Favorites!"
        );

    }

}


// ---------- Load Saved Data ----------

function loadSavedData() {

    const savedName =
        localStorage.getItem(
            "flashcardUsername"
        );

    if (savedName) {

        usernameInput.value =
            savedName;

    }

    const savedFavorites =
        localStorage.getItem(
            "flashcardFavorites"
        );

    if (savedFavorites) {

        favorites =
            JSON.parse(savedFavorites);

    }

}


// ---------- Keyboard Controls ----------

document.addEventListener(
    "keydown",
    function (event) {

        if (event.key === "Enter") {

            if (
                document.activeElement ===
                usernameInput
            ) {

                continueButton.click();

            }

        }

        if (event.key === "1") {

            correctButton.click();

        }

        if (event.key === "2") {

            wrongButton.click();

        }

    }
);


// ---------- Initialize ----------

loadSavedData();

loadFlashcard();

updateProgress();