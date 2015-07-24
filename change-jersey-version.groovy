import groovy.io.*
import java.util.regex.*

if (args.length < 2) {
    println 'Usage: groovy change-jersey-version.groovy <old> <new>'
    return
}

def oldVersion = args[0]
def newVersion = args[1]

def p = Pattern.compile("(.*jersey.*:)${oldVersion}('.*)")

new File('.').traverse(nameFilter: 'build.gradle') {
    def lines = new ArrayList()
    it.withReader('UTF-8') { ins ->
        ins.eachLine { line ->
            def m = p.matcher(line)
            if (m.matches()) {
                def newLine = m.group(1) + newVersion + m.group(2)
                lines += newLine
            } else {
                lines += line
            }
        }
    } 
    it.withWriter('UTF-8') { out ->
        lines.each {
            out.writeLine(it)
        }
    }
}
