from PyQt5 import QtWidgets, QtCore, QtGui
import sys, random, os
import re

class Question:
    def __init__(self, text, options, answer, explanation, qtype="MCQ", image_path=None, explanation_image_path=None):
        self.text = text
        self.options = options
        self.answer = answer
        self.explanation = explanation
        self.qtype = qtype
        self.image_path = image_path
        self.explanation_image_path = explanation_image_path

class QuizApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Quiz Generator - MCQ/MSQ/NAT")
        self.setMinimumSize(850, 700)
        self.resize(850, 700)

        self.setStyleSheet("QLabel { font-size: 16pt; } \
            QRadioButton { font-size: 16pt; }\
            QCheckBox { font-size: 16pt; }\
            QPushButton { font-size: 14pt;}\
            QTextEdit { font-size: 14pt;}\
            QLineEdit { font-size: 14pt;} ")

        self.questions = []
        self.current_index = 0
        self.user_answers = {}
        self.total_questions = 0
        self.review_mode = False

        self.layout = QtWidgets.QVBoxLayout(self)

        self.file_name_label = QtWidgets.QLabel("No quiz loaded")
        self.file_name_label.setAlignment(QtCore.Qt.AlignCenter)
        font = QtGui.QFont()
        font.setBold(True)
        self.file_name_label.setFont(font)
        self.layout.addWidget(self.file_name_label)

        self.load_btn = QtWidgets.QPushButton("Load Quiz")
        self.load_btn.clicked.connect(self.load_quiz)
        self.layout.addWidget(self.load_btn)

        self.question_label = QtWidgets.QLabel("")
        self.question_label.setWordWrap(True)
        self.layout.addWidget(self.question_label)

        self.image_label = QtWidgets.QLabel()
        self.image_label.setAlignment(QtCore.Qt.AlignCenter)
        self.layout.addWidget(self.image_label)

        self.options_group = QtWidgets.QButtonGroup(self)
        self.options_layout = QtWidgets.QVBoxLayout()
        self.option_widgets = []
        self.layout.addLayout(self.options_layout)

        self.nat_input = QtWidgets.QLineEdit()
        self.layout.addWidget(self.nat_input)
        self.nat_input.hide()

        nav_layout = QtWidgets.QHBoxLayout()
        self.prev_btn = QtWidgets.QPushButton("Previous")
        self.prev_btn.clicked.connect(self.prev_question)
        nav_layout.addWidget(self.prev_btn)

        self.next_btn = QtWidgets.QPushButton("Next")
        self.next_btn.clicked.connect(self.next_question)
        nav_layout.addWidget(self.next_btn)
        self.layout.addLayout(nav_layout)

        self.submit_btn = QtWidgets.QPushButton("Submit Quiz")
        self.submit_btn.clicked.connect(self.submit_quiz)
        self.layout.addWidget(self.submit_btn)

        self.explanation_area = QtWidgets.QTextEdit()
        self.explanation_area.setReadOnly(True)
        self.explanation_area.hide()
        self.layout.addWidget(self.explanation_area)

        self.toggle_quiz_controls(False)

    def toggle_quiz_controls(self, enable):
        self.question_label.setVisible(enable)
        self.image_label.setVisible(False)
        self.nat_input.setVisible(enable)
        self.prev_btn.setEnabled(enable)
        self.next_btn.setEnabled(enable)
        self.submit_btn.setEnabled(enable)
        for w in self.option_widgets:
            w.setVisible(enable)

    # UPDATED: The load_quiz function is modified to handle multiline questions.
    def load_quiz(self):
        self.questions.clear()
        self.user_answers.clear()
        self.explanation_area.clear()
        self.explanation_area.hide()
        self.review_mode = False
        self.toggle_quiz_controls(False)
        self.question_label.clear()

        file_path, _ = QtWidgets.QFileDialog.getOpenFileName(self, "Select Quiz File", "", "Text Files (*.txt)")
        if not file_path:
            return
        try:
            self.file_name_label.setText(os.path.basename(file_path))
            with open(file_path, "r", encoding="utf-8") as file:
                blocks = file.read().strip().split("\n\n")
            
            quiz_dir = os.path.dirname(file_path)

            for block in blocks:
                lines = block.strip().split("\n")
                if not lines:
                    continue
                
                # --- Find where the question text ends ---
                first_line = lines[0]
                qtype = first_line.split(":")[0].strip()
                
                question_text_lines = [first_line.split(":", 1)[1].strip()]
                
                # Find the starting index of options or answer
                content_start_index = 1
                for i, line in enumerate(lines[1:], 1):
                    stripped_line = line.strip()
                    # Options/Answer act as delimiters for the question text
                    is_option = qtype in ["MCQ", "MSQ"] and stripped_line.startswith(('A)', 'B)', 'C)', 'D)'))
                    is_answer = stripped_line.startswith('Answer:')
                    
                    if is_option or is_answer:
                        content_start_index = i
                        break
                    question_text_lines.append(line)
                else: # No break occurred, meaning no options/answer found after question
                    content_start_index = len(lines)
                
                q_text_raw = "\n".join(question_text_lines).strip()
                
                # --- Parse Question Image ---
                image_path = None
                q_text = q_text_raw
                image_match = re.search(r'\[image:\s*(.*?)\s*\]', q_text_raw)
                if image_match:
                    image_filename = image_match.group(1)
                    image_path = os.path.join(quiz_dir, image_filename)
                    q_text = q_text_raw.replace(image_match.group(0), "").strip()

                # --- Parse Options, Answer, and Explanation ---
                options = []
                answer = ""
                explanation_index = -1

                if qtype in ["MCQ", "MSQ"]:
                    options = [line[3:].strip() for line in lines[content_start_index : content_start_index + 4]]
                    answer = lines[content_start_index + 4].split(":")[-1].strip()
                    explanation_index = content_start_index + 5
                elif qtype == "NAT":
                    answer = lines[content_start_index].split(":")[-1].strip()
                    explanation_index = content_start_index + 1
                else:
                    continue
                
                # --- Parse Explanation and Explanation Image ---
                raw_explanation = "\n".join(lines[explanation_index:]).replace("Explanation:", "").strip() if len(lines) > explanation_index else "No explanation provided."
                explanation_image_path = None
                explanation_text = raw_explanation
                expl_image_match = re.search(r'\[image:\s*(.*?)\s*\]', raw_explanation)
                if expl_image_match:
                    expl_image_filename = expl_image_match.group(1)
                    explanation_image_path = os.path.join(quiz_dir, expl_image_filename)
                    explanation_text = raw_explanation.replace(expl_image_match.group(0), "").strip()
                
                self.questions.append(Question(q_text, options, answer, explanation_text, qtype, image_path, explanation_image_path))

            total_available = len(self.questions)
            num, ok = QtWidgets.QInputDialog.getInt(self, "Number of Questions", f"Enter number of questions (max {total_available}):", min=1, max=total_available)
            if ok:
                random.seed()
                random.shuffle(self.questions)
                self.total_questions = num
                self.current_index = 0
                self.toggle_quiz_controls(True)
                self.show_question()
        except Exception as e:
            QtWidgets.QMessageBox.critical(self, "Error", f"Failed to load quiz: {e}")


    def show_question(self):
        if not self.questions:
            return
        q = self.questions[self.current_index]
        self.question_label.setText(f"Q{self.current_index+1}: {q.text}")
        
        self.image_label.clear()
        if q.image_path and os.path.exists(q.image_path):
            pixmap = QtGui.QPixmap(q.image_path)
            
            if pixmap.width() > 800 or pixmap.height() > 600:
                pixmap = pixmap.scaled(800, 600, QtCore.Qt.KeepAspectRatio, QtCore.Qt.SmoothTransformation)
            
            self.image_label.setPixmap(pixmap)
            self.image_label.show()
        else:
            self.image_label.hide()

        for w in self.option_widgets:
            self.options_layout.removeWidget(w)
            w.deleteLater()
        self.option_widgets.clear()
        self.options_group = QtWidgets.QButtonGroup(self)

        self.nat_input.hide()
        if q.qtype == "MCQ":
            for i, opt in enumerate(q.options):
                rb = QtWidgets.QRadioButton(f"{chr(65+i)}) {opt}")
                self.options_group.addButton(rb, i)
                self.options_layout.addWidget(rb)
                self.option_widgets.append(rb)
                if self.review_mode:
                    if chr(65+i) == q.answer:
                        rb.setStyleSheet("color: green;")
                    elif self.user_answers.get(self.current_index) == chr(65+i):
                        rb.setStyleSheet("color: red;")
            sel = self.user_answers.get(self.current_index)
            if sel:
                for btn in self.option_widgets:
                    btn.setChecked(btn.text().startswith(sel))

        elif q.qtype == "MSQ":
            for i, opt in enumerate(q.options):
                cb = QtWidgets.QCheckBox(f"{chr(65+i)}) {opt}")
                self.options_layout.addWidget(cb)
                self.option_widgets.append(cb)
                if self.review_mode:
                    if chr(65+i) in q.answer.split(","):
                        cb.setStyleSheet("color: green;")
                    elif chr(65+i) in self.user_answers.get(self.current_index, []):
                        cb.setStyleSheet("color: red;")
            sel = self.user_answers.get(self.current_index, [])
            for cb in self.option_widgets:
                cb.setChecked(cb.text()[0] in sel)

        elif q.qtype == "NAT":
            self.nat_input.show()
            self.nat_input.setText(self.user_answers.get(self.current_index, ""))
            if self.review_mode:
                if self.nat_input.text() == q.answer:
                    self.nat_input.setStyleSheet("color: green;")
                else:
                    self.nat_input.setStyleSheet("color: red;")

    def save_answer(self):
        if self.review_mode:
            return
        q = self.questions[self.current_index]
        if q.qtype == "MCQ":
            checked_id = self.options_group.checkedId()
            if checked_id >= 0:
                self.user_answers[self.current_index] = chr(65 + checked_id)
        elif q.qtype == "MSQ":
            selected = [cb.text()[0] for cb in self.option_widgets if cb.isChecked()]
            self.user_answers[self.current_index] = selected
        elif q.qtype == "NAT":
            self.user_answers[self.current_index] = self.nat_input.text().strip()

    def next_question(self):
        self.save_answer()
        if self.current_index < self.total_questions - 1:
            self.current_index += 1
            self.show_question()

    def prev_question(self):
        self.save_answer()
        if self.current_index > 0:
            self.current_index -= 1
            self.show_question()

    def submit_quiz(self):
        self.save_answer()
        wrong_answers = []
        score = 0
        for i, q in enumerate(self.questions[:self.total_questions]):
            ans = self.user_answers.get(i, "")
            if q.qtype == "MCQ" and ans == q.answer:
                score += 1
            elif q.qtype == "MSQ" and set(ans) == set(q.answer.split(",")):
                score += 1
            elif q.qtype == "NAT" and ans == q.answer:
                score += 1
            else:
                wrong_answers.append((q, ans))

        self.review_mode = True
        self.show_question()

        self.explanation_area.clear()
        self.explanation_area.show()
        self.explanation_area.append(f"<b>Your score: {score}/{self.total_questions}</b><br><br>")
        if wrong_answers:
            self.explanation_area.append("<b>Questions you got wrong:</b><br>")
            for q, ans in wrong_answers:
                image_info = ""
                if q.image_path:
                    image_info = f" (Image: {os.path.basename(q.image_path)})"
                
                self.explanation_area.append(f"<span style='color:red;'>Q: {q.text}{image_info}</span><br>")
                self.explanation_area.append(f"<span style='color:orange;'>Your Answer: {ans}</span><br>")
                self.explanation_area.append(f"<span style='color:green;'>Correct Answer: {q.answer}</span><br>")

                explanation_html = f"<span style='color:blue;'>Explanation: {q.explanation}</span>"
                if q.explanation_image_path and os.path.exists(q.explanation_image_path):
                    image_url = QtCore.QUrl.fromLocalFile(q.explanation_image_path).toString()
                    explanation_html += f'<br><img src="{image_url}" style="max-width:800px; max-height:600px; width:auto; height:auto;">'
                
                self.explanation_area.append(explanation_html)
                self.explanation_area.append("<br><br>")

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    window = QuizApp()
    window.show()
    sys.exit(app.exec_())