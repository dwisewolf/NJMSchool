package com.wisewolf.njmschool.Models;

import java.util.List;

public class QuizQuestion
{
    private String[] asnwers_list;

    private String[][] options_list;

    private String[] questions_list;

    private String[] flag_list;

    private String[] url_list;

    public String[] getFlag_list() {
        return flag_list;
    }

    public void setFlag_list(String[] flag_list) {
        this.flag_list = flag_list;
    }

    public String[] getUrl_list() {
        return url_list;
    }

    public void setUrl_list(String[] url_list) {
        this.url_list = url_list;
    }

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
