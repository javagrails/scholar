package bk.scholar.app.service;

import bk.scholar.app.domain.Student;
import bk.scholar.app.pepository.StudentRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private static final Logger log = LoggerFactory.getLogger(StudentService.class);
  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @Tool(name = "find_a_student", description = "Get a student by its ID")
  public Student findById(Long id) {
    log.info("find_a_student | Student: {}", id);
    return studentRepository.findById(id).orElse(null);
  }

  @Tool(name = "retrieve_students", description = "Get all students")
  public List<Student> findAllStudents() {
    log.info("find_all_student | Get all students");
    return studentRepository.findAll();
  }


}
