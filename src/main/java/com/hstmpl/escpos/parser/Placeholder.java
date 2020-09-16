package com.hstmpl.escpos.parser;

public class Placeholder {

    private int start;
    private int end;
    private Variable variable;

    public Placeholder(int start, int end, String variable) {
        this.start = start;
        this.end = end;
        this.variable = Variable.of(variable);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Variable getVariable() {
        return variable;
    }

    public <T> T execute(Transform transform, Object env) {
        return variable.execute(transform, env);
    }

    @Override
    public String toString() {
        return "Placeholder{" +
                "start=" + start +
                ", end=" + end +
                ", variable=" + variable +
                '}';
    }
}
