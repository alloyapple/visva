package com.example.ailatrieuphu_visva.db;

import java.util.Random;

public class Question {
	private String _question_content;
	private String[] _answer = new String[4];
	private String _note;
	private int _correct_answer;
	private int _id;
	private int level;
	private Random random = new Random();

	public String get_question_content() {
		return _question_content;
	}

	public void set_question_content(String _question_content) {
		this._question_content = _question_content;
	}

	public void set_answer_a(String _answer_a) {
		this._answer[0] = _answer_a;
	}

	public String get_answer_a() {
		return this._answer[0];
	}

	public void set_answer_b(String _answer_b) {
		this._answer[1] = _answer_b;
	}

	public String get_answer_b() {
		return this._answer[1];
	}

	public void set_answer_c(String _answer_c) {
		this._answer[2] = _answer_c;
	}

	public String get_answer_c() {
		return this._answer[2];
	}

	public void set_answer_d(String _answer_d) {
		this._answer[3] = _answer_d;
	}

	public String get_answer_d() {
		return this._answer[3];
	}

	public String get_answer(int i) {
		return this._answer[i];
	}

	public String[] get_answer() {
		return _answer;
	}

	public String get_note() {
		return _note;
	}

	public void set_note(String _note) {
		this._note = _note;
	}

	public int get_correct_answer() {
		return _correct_answer;
	}

	public void set_correct_answer(int _correct_answer) {
		this._correct_answer = _correct_answer;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public void swap_correct_answer() {
		String temp = _answer[_correct_answer - 1];
		int i = random.nextInt(4);
		_answer[_correct_answer - 1] = _answer[i];
		_answer[i] = temp;
		_correct_answer = i + 1;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
