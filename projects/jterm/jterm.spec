Buildroot: /.
Name: jterm
Version: 0.16
Release: 1
Summary: Java Terminal Emulator
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, jsch, jzlib, openssh-server
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Java Terminal Emulator

%files
