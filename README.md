# hadoop-hands-on


## Generate user data
- http://api.randomuser.me/?results=100&nat=us&exc=location,id,picture&&fmt=csv&noinfo&dl

## Generate access log
```
# garbagetown at garbagetown.local in ~/dev/repos [17:02:10]
$ g clone git@github.com:tamtam180/apache_log_gen.git
Cloning into 'apache_log_gen'...
remote: Counting objects: 86, done.
remote: Total 86 (delta 0), reused 0 (delta 0), pack-reused 86
Receiving objects: 100% (86/86), 23.69 KiB | 0 bytes/s, done.
Resolving deltas: 100% (29/29), done.
Checking connectivity... done.

# garbagetown at garbagetown.local in ~/dev/repos [17:02:25]
$ cd apache_log_gen

# garbagetown at garbagetown.local in ~/dev/repos/apache_log_gen on git:master o [17:02:35]
$ gem install apache-loggen
Fetching: apache-loggen-0.0.4.gem (100%)
Successfully installed apache-loggen-0.0.4
1 gem installed
Installing ri documentation for apache-loggen-0.0.4...
Installing RDoc documentation for apache-loggen-0.0.4...

# garbagetown at garbagetown.local in ~/dev/repos/apache_log_gen on git:master o [17:02:51]
$ cd ~/dev/repos/hadoop-hands-on

# garbagetown at garbagetown.local in ~/dev/repos/hadoop-hands-on on git:master x [17:04:11]
$ ruby src/scripts/my_generator.rb --limit=1000 src/main/resources/access.log
```