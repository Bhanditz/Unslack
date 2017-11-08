package vincentcarrier.todo.models

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

@Entity
class Task {
  @Id(assignable = true)
  var id: Long = 0

  lateinit var name: String

  @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
  lateinit var dateAdded: LocalDateTime

  lateinit var project: ToOne<Project>

  // For ObjectBox
  constructor() {}

  // For user
  constructor(name: String, projectId: Long) {
    this.name = name
    this.dateAdded = LocalDateTime()
    this.project.targetId = projectId
  }

  // For Moshi
  internal constructor(json: TaskJson) {
    this.id = json.id
    this.name = json.content
    this.dateAdded = LocalDateTime.parse(json.date_added,
        DateTimeFormat.forPattern("EEE dd MMM yyyy HH:mm:ss Z"))
    this.project.targetId = json.project_id
  }
}

internal class TaskJson {
  val id: Long
  val content: String
  val date_added: String
  val project_id: Long

  constructor(id: Long, content: String, date_added: String, project_id: Long) {
    this.id = id
    this.content = content
    this.date_added = date_added
    this.project_id = project_id
  }

  constructor(task: Task) {
    this.id = task.id
    this.content = task.name
    this.date_added = task.dateAdded.toString("EEE dd MMM yyyy HH:mm:ss Z")
    this.project_id = task.project.targetId
  }
}

internal data class TaskJson(
    val id: Long,
    val content: String,
    val date_added: String,
    val project_id: Long
)