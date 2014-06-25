Buildroot: /.
Name: jlogon
Version: 0.1
Release: 1
Summary: Java Startup and Logon
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, xorg-x11-server-Xorg
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%post
#! /bin/sh

set -e

chkconfig --add jlogond

%preun
#!/bin/sh

set -e

if [ -x "/etc/init.d/jlogond" ]; then
#  service jlogond stop || exit $?
fi

%postun
#!/bin/sh -e

chkconfig --del jlogond

%description

Java Startup and Logon Process

%files
