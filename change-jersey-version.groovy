import groovy.io.*
import java.util.regex.*

if (args.length < 1) {
    println 'Usage: groovy change-jersey-version.groovy <new version>'
    return
}

def newVersion = args[0]

def p = Pattern.compile(/([^']+'org\.glassfish\.jersey[^:]*:[^:]+:).+(')/)

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
