package sample.domain;

public class Student implements HasId<Integer> {

    private int id, group;
    private String name, email, teacherName;

    /**
     *
     * @param id
     * @param name
     * @param group
     * @param email
     * @param teacherName
     */
    public Student(int id, String name, int group, String email, String teacherName) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.email = email;
        this.teacherName = teacherName;
    }

    /**
     * set Id
     * @param id
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get Id
     * @return
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * set Group
     * @param group
     */
    public void setGroup(int group) {
        this.group = group;
    }

    /**
     * set name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * set teacher name
     * @param teacherName
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * get group
     * @return
     */
    public int getGroup() {
        return group;
    }

    /**
     * get name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * get teacher name
     * @return
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * overrie toString
     * @return student
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", group=" + group +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
