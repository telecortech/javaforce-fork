Buildroot: /.
Name: jremote
Version: 0.1
Release: 1
Summary: Java Remote Desktop Manager
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, rdesktop, jvnc
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Java Remote Desktop Manager

%files
