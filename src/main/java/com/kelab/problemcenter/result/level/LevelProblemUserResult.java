package com.kelab.problemcenter.result.level;

import java.util.List;

public class LevelProblemUserResult {

    private String name;

    private List<Detail> details;

    private Integer grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public static class Detail {

        private boolean success;

        private String name;

        private Integer status;

        private List<ProblemResult> problems;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public List<ProblemResult> getProblems() {
            return problems;
        }

        public void setProblems(List<ProblemResult> problems) {
            this.problems = problems;
        }
    }
}