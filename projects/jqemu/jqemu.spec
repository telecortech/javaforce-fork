Buildroot: /.
Name: jqemu
Version: 0.2
Release: 1
Summary: Java Virtual Machine Manager (QEMU)
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, qemu, jvnc
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Java Virtual Machine Manager (QEMU)

%post

chkconfig --add jqemud
service jqemud start

%postun

chkconfig --del jqemud

%files
