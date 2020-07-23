package com.wisewolf.njmschool.Models;

import java.util.List;

public class QuizQuestion
{
    private String[] asnwers_list;

    private String[][] options_list;

    private String[] questions_list;

    public String[] getAsnwers_list ()
    {
        return asnwers_list;
    }

    public void setAsnwers_list (String[] asnwers_list)
    {
        this.asnwers_list = asnwers_list;
    }

    public String[][] getOptions_list ()
    {
        return options_list;
    }

    public void setOptions_list (String[][] options_list)
    {
        this.options_list = options_list;
    }

    public String[] getQuestions_list ()
    {
        return questions_list;
    }

    public void setQuestions_list (String[] questions_list)
    {
        this.questions_list = questions_list;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [asnwers_list = "+asnwers_list+", options_list = "+options_list+", questions_list = "+questions_list+"]";
    }
}
