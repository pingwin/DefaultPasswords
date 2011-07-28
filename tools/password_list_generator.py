#!/usr/bin/env python2

import xml.dom.minidom, urllib2

def main():
    url = 'http://www.phenoelit-us.org/dpl/dpl.html'
    try:
        contents = open("/tmp/deleted_dpl.html").read()
    except:
        contents = urllib2.urlopen(url).read()
        open("/tmp/deleted_dpl.html", 'w').write(contents)
    dom = xml.dom.minidom.parseString(contents)
    mapit = []
    count = 0
    print "drop table android_metadata;"
    print "drop table default_passwords;"
    print "CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US');"
    print "INSERT INTO android_metadata VALUES ('en_US');"
    print "CREATE TABLE default_passwords (_id integer primary key autoincrement, "
    for row in dom.getElementsByTagName('tr'):
        if not count:
            for itm in row.getElementsByTagName('td'):
                mapit.append("`%s`" % itm.lastChild.lastChild.data.replace(' ', '_'))
            print ",".join(["%s varchar(64)" % it for it in mapit])
            print "); "
            count += 1
            continue
        try:
            ins  = "INSERT INTO default_passwords ("
            ins += ",".join(mapit)
            ins += ") VALUES ("
            tmpRow = []
            for itm in row.getElementsByTagName('td'):
                if not itm.lastChild:
                    tmpRow.append("NULL")
                    continue
                tmpRow.append('"%s"' % itm.lastChild.data)
            ins += ",".join(tmpRow)
            ins += ");"
            print ins
        except Exception, inst:
            pass
    print ";"

if __name__ == "__main__":
    main()
