$(document).ready(function() {
    let workDuration = 25 * 60; // 25 minutes in seconds
    let breakDuration = 5 * 60; // 5 minutes in seconds
    let timeRemaining = workDuration;
    let timerInterval;
    let isBreak = false;
    let isRunning = false;

    // Update the timer display
    function updateTimerDisplay() {
        const minutes = Math.floor(timeRemaining / 60);
        const seconds = timeRemaining % 60;
        $('#timer-display').text(`${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`);
    }

    // Show messages to the user
    function showMessage(message, type) {
        const messageContainer = $("#message");

        const alertBox = $(`
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `);

        messageContainer.html(alertBox);
        setTimeout(() => {
            alertBox.alert('close');
        }, 3000);
    }

    // Start the timer
    function startTimer() {
        isRunning = true;
        $('#start-btn').text("Pause");
        $('#end-session-btn').attr('disabled', false);

        timerInterval = setInterval(function() {
            timeRemaining--;
            updateTimerDisplay();

            if (timeRemaining <= 0) {
                clearInterval(timerInterval);

                if (isBreak) {
                    showMessage("Break is over! Time to work!", "success");
                    timeRemaining = workDuration;
                    isBreak = false;
                    updateTimerDisplay();
                    startTimer(); // Restart work timer after break
                } else {
                    showMessage("Pomodoro completed! Time for a break.", "info");
                    timeRemaining = breakDuration;
                    isBreak = true;
                    updateTimerDisplay();
                    startTimer(); // Automatically start break after work
                }
            }
        }, 1000);
    }

    // Pause the timer
    function pauseTimer() {
        isRunning = false;
        clearInterval(timerInterval);
        $('#start-btn').text("Resume");
        $('#end-session-btn').attr('disabled', false);
    }

    // Reset the timer
    function resetTimer() {
        clearInterval(timerInterval);
        isRunning = false;
        timeRemaining = workDuration;
        isBreak = false;
        updateTimerDisplay();
        $('#start-btn').text("Start");
        $('#end-session-btn').attr('disabled', false);
    }

    // End the current session early
    function endSession() {
        clearInterval(timerInterval);
        showMessage("Session ended early.", "warning");
        resetTimer();
    }

    // Save the user's custom durations
    function saveChanges() {
        const workInput = $('#work-duration').val();
        const breakInput = $('#break-duration').val();

        if (workInput <= 0 || breakInput <= 0 || isNaN(workInput) || isNaN(breakInput)) {
            showMessage("Invalid input! Please enter positive values for durations.", "danger");
            return;
        }

        workDuration = workInput * 60; // Convert minutes to seconds
        breakDuration = breakInput * 60; // Convert minutes to seconds
        timeRemaining = workDuration; // Reset to work session

        showMessage("Session and break durations updated successfully!", "success");
        updateTimerDisplay();
    }

    // Button actions
    $('#start-btn').click(function() {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    });

    $('#reset-btn').click(function() {
        resetTimer();
    });

    $('#end-session-btn').click(function() {
        endSession();
    });

    // Save custom durations when the user clicks "Save Changes"
    $('#save-btn').click(function() {
        saveChanges();
    });

    // Initialize the timer display
    updateTimerDisplay();
});
