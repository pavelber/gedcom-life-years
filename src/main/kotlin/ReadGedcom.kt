
import org.gedcom4j.model.Gedcom
import org.gedcom4j.model.Individual
import org.gedcom4j.model.IndividualEventType
import org.gedcom4j.parser.GedcomParser
import java.util.*

object ReadGedcom {
    @JvmStatic
    fun main(args: Array<String>) {
        val gp = GedcomParser()
        gp.load("c:\\temp\\1.ged")
        val g = gp.gedcom
        val filtered1 = g.individuals.values.filter { p-> hasEvent(p,IndividualEventType.BIRTH) && hasEvent(p,IndividualEventType.DEATH) }
        println(filtered1.size)
        val filtered2 = filtered1.filter { p-> getYear(p,IndividualEventType.DEATH)>1945 || getYear(p,IndividualEventType.DEATH) < 1941}
        println(filtered2.size)
        val people = filtered2.map{i->Person(getYear(i,IndividualEventType.BIRTH), getYear(i,IndividualEventType.DEATH))}
        val grouped = people.groupBy { p->p.birthYear }.toSortedMap()
        grouped.keys.forEach{year-> println("$year\t${average(grouped.get(year))}")}

    }

    private fun average(list: List<Person>?): Int {
       return  list!!.sumBy { p->p.deathYear-p.birthYear }/(list!!.size)
    }

    data class Person(val birthYear:Int, val deathYear:Int)

    fun hasEvent(p:Individual, event:IndividualEventType): Boolean {
        val events = p.getEventsOfType(event)
        if (events.size>0)
            return events[0].date!=null
        return false
    }

    fun getYear(p:Individual, event:IndividualEventType): Int {
        val events = p.getEventsOfType(event)
        if (events.size>1) println(p.names)
        //println(events[0].date)
        return events[0].date.value.takeLast(4).toInt()
    }


}