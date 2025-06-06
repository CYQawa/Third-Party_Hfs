package com.hfs.cyq.Assistings;

import java.io.Serializable;
import java.util.List;

public class ExamModel {
  // ExamDetailResponse.java
  public class ExamDetailResponse {
    private int code;
    private String msg;
    private Exam data; // 直接是Exam对象，不再是包含list的结构

    // getters & setters
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

  //  public class ExamData {
  //    private List<Exam> list;
  //    private int totalCount;
  //    private String limitTip;
  //
  //    // Getters and Setters
  //
  //    public List<Exam> getList() {
  //      return this.list;
  //    }
  //
  //    public int getTotalCount() {
  //      return this.totalCount;
  //    }
  //
  //    public String getLimitTip() {
  //      return this.limitTip;
  //    }
  //  }

  // Exam.java
  public class Exam implements Serializable {
    private long examId;
    private String name;
    private String time; // 类型改为String
    private int manfen;
    private int manfenBeforeGrading;
    private String score;
    private String scoreBeforeGrading;
    private String classRank;
    private List<Paper> papers;
    private int scoreMedal;
    private String level;
    private double scoreRate;
    private String difficultyLevel;
    private Compare compare;
    private int progressMedal;

    // getters & setters

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
  }

  // Paper.java
  public class Paper implements Serializable {
    private String paperId; // 字段名变更
    private String pid;
    private String name;
    private String subject;
    private int manfen;
    private int manfenBeforeGrading;
    private String score; // 注意是String类型
    private String scoreBeforeGrading;
    private int gradingType;
    private int subPaperType;
    private int star;

    // getters & setters

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
    private long id;
    private String name;
    private String time;
    private int type;
    private int curSocre;
    private String score;
    private int scoreWave;
    private String classRank;
    private int classRankWave;
    private int curGradeRank;
    private int preGradeRank;
    private String gradeRank;
    private int gradeRankWave;
    private int scoreRange;
    private int gradeRankRange;
    private int classRankRange;
    private double preScoreRate;
    private int dlWave;
    private int matchType;

    // getters & setters

    public int getCurGradeRank() {
      return this.curGradeRank;
    }
  }

  public class ExamResponseData {
    private int code;
    private String msg;
    private Data data;

    // 嵌套类
    public class Data {
      private List<Exam> list;

      public List<Exam> getList() {
        return list;
      }
    }

    public class Exam {

      private long examId; // 对应 examId
      private String name; // 对应考试名字
      private String score; // 对应总分（JSON 中是字符串类型）
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

    // Getter 方法
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

    // 生成 getter/setter 方法
    public int getCode() {
      return code;
    }

    public String getMsg() {
      return msg;
    }

    public UserData getData() {
      return data;
    }

    // 判断请求是否成功的方法
    public boolean isSuccess() {
      return code == 0; // 根据你的业务逻辑判断
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

    // Getter 方法
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
}
