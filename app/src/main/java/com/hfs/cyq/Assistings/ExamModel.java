package com.hfs.cyq.Assistings;

import java.io.Serializable;
import java.util.List;

public class ExamModel {
  // ExamDetailResponse.java
  public class ExamDetailResponse {
    private int code;
    private String msg;
    private Exam data;

    public int getCode() {
      return this.code;
    }

    public String getMsg() {
      return this.msg;
    }

    public Exam getData() {
      return this.data;
    }
  }
  
  public class Exam implements Serializable {
    private long examId;
    private String name;
    private int manfen;
    private String score;
    private String classRank;
    private List<Paper> papers;
    private String level;
    private Compare compare;

    public List<Paper> getPapers() {
      return this.papers;
    }

    public void setPapers(List<Paper> papers) {
      this.papers = papers;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getManfen() {
      return this.manfen;
    }

    public String getScore() {
      return this.score;
    }

    public String getClassRank() {
      return this.classRank;
    }

    public String getLevel() {
      return this.level;
    }

    public Compare getCompare() {
      return this.compare;
    }

    public void setCompare(Compare compare) {
      this.compare = compare;
    }

    public long getExamId() {
      return this.examId;
    }

    public void setExamId(long examId) {
      this.examId = examId;
    }
  }

  // Paper.java
  public class Paper implements Serializable {
    private String paperId;
    private String name;
    private String subject;
    private int manfen;
    private String score;

    public String getSubject() {
      return this.subject;
    }

    public String getPaperId() {
      return this.paperId;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getManfen() {
      return this.manfen;
    }

    public void setManfen(int manfen) {
      this.manfen = manfen;
    }

    public String getScore() {
      return this.score;
    }

    public void setScore(String score) {
      this.score = score;
    }
  }

  // 新增Compare类
  public class Compare implements Serializable {
    private int curGradeRank;

    public int getCurGradeRank() {
      return this.curGradeRank;
    }
  }

  public class PaperOverview implements Serializable {
    private Data data;

    public class Data {
      private Compare compare;
      private String level;

      public class Compare {
        private int cr;
        private int gr;

        public int getCr() {
          return cr;
        }

        public int getGr() {
          return gr;
        }
      }

      public Compare getCompare() {
        return this.compare;
      }

      public String getLevel() {
        return this.level;
      }

      public void setLevel(String level) {
        this.level = level;
      }
    }

    public Data getData() {
      return this.data;
    }

    public void setData(Data data) {
      this.data = data;
    }
  }

  public class ExamResponseData {
    private int code;
    private String msg;
    private Data data;

    public class Data {
      private List<Exam> list;

      public List<Exam> getList() {
        return list;
      }
    }

    public class Exam {
      private long examId;
      private String name;
      private String score;
      private Long time;

      public long getExamId() {
        return examId;
      }

      public long getTime() {
        return time;
      }

      public String getName() {
        return name;
      }

      public String getScore() {
        return score;
      }
    }

    public Data getData() {
      return data;
    }

    public String getMsg() {
      return msg;
    }

    public int getCode() {
      return code;
    }
  }

  public class UserData {
    private String token;

    public String getToken() {
      return token;
    }
  }

  public class ApiResponse {
    private int code;
    private String msg;
    private UserData data;

    public int getCode() {
      return code;
    }

    public String getMsg() {
      return msg;
    }

    public UserData getData() {
      return data;
    }

    public boolean isSuccess() {
      return code == 0;
    }
  }

  public static class ExamListdata {
    private String id;
    private String name;
    private String time;

    public ExamListdata(String id, String name, String time) {
      this.id = id;
      this.name = name;
      this.time = time;
    }

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getTime() {
      return time;
    }
  }

  public class Userinformation {
    private Data data;
    public Data getData() {
      return data;
    }
    public class Data {
      private String avatar;
      private LinkedStudent linkedStudent;

      public String getAvatar() {
        return avatar;
      }

      public LinkedStudent getLinkedStudent() {
        return linkedStudent;
      }
    }

    public class LinkedStudent {
      private String schoolName;
            private String studentName;
      private String grade;

      public String getSchoolName() {
        return schoolName;
      }

            public String getStudentName() {
                return studentName;
            }
      public String getGrade() {
        return grade;
      }
    }
  }
}
