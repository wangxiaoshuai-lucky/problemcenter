package com.kelab.problemcenter.result.level;

import java.util.List;

public class LevelProblemAdminResult {

    private Integer id;

    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}