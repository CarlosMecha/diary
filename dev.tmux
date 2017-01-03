
# Create session
new-session -Ad -c ~/Workspace/Diary/ -n src -s diary '/bin/bash --rcfile devrc'
set-option -t diary status-style "bg=colour235"
set-option -a -t diary status-style "fg=colour3"
set-option -t diary default-command "/bin/bash --rcfile devrc"

# Windows

## 1. git
new-window -n git -t diary

## 2. db
new-window -n db -t diary

## 3. mvn
new-window -n mvn -t diary

## 4. test
new-window -n test -t diary

## 5. deploy
new-window -n deploy -t diary

## 6. cloud
new-window -n cloud -t diary

# Notification
display "Session diary created"

