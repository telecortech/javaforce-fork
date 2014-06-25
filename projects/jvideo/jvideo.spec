Buildroot: /.
Name: jvideo
Version: 0.13
Release: 1
Summary: Video Creator
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, ffmpeg
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Video Creator

%files
