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
    print "CREATE TABLE default_passwords ("
    for row in dom.getElementsByTagName('tr'):
        if not count:
            for itm in row.getElementsByTagName('td'):
                mapit.append("`%s`" % itm.lastChild.lastChild.data)
            print ",".join(["%s varchar(64)" % it for it in mapit])
            print "); "
            count += 1
            continue
        print "INSERT INTO default_passwords ("
        print ",".join(mapit)
        print ") VALUES "
        tmpRow = []
        print "("
        for itm in row.getElementsByTagName('td'):
            if not itm.lastChild:
                tmpRow.append("NULL")
                continue
            tmpRow.append('"%s"' % itm.lastChild.data)
        print ",".join(tmpRow)
        print ");"
    print ";"

if __name__ == "__main__":
    main()
