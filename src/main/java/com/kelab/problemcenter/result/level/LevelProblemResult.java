package com.kelab.problemcenter.result.level;

import java.util.List;

public class LevelProblemResult {

    private Integer id;


    private List<Grade> grades;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public static class Grade {
        private Integer number;

        private String name;

        private List<ProblemResult> problems;

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ProblemResult> getProblems() {
            return problems;
        }

        public void setProblems(List<ProblemResult> problems) {
            this.problems = problems;
        }
    }

    public static class ProblemResult {
        private Integer id;

        private Integer proId;

        private Integer levelId;

        private Integer grade;

        private String title;

        private boolean ac;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getProId() {
            return proId;
        }

        public void setProId(Integer proId) {
            this.proId = proId;
        }

        public Integer getLevelId() {
            return levelId;
        }

        public void setLevelId(Integer levelId) {
            this.levelId = levelId;
        }

        public Integer getGrade() {
            return grade;
        }

        public void setGrade(Integer grade) {
            this.grade = grade;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isAc() {
            return ac;
        }

        public void setAc(boolean ac) {
            this.ac = ac;
        }
    }
}