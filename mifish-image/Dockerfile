FROM centos:centos8
MAINTAINER ruanyi <ruanyi@mifish.com>

# User root user to install software
USER root

#解决 Error: Failed to download metadata for repo 'appstream': Cannot prepare internal mirrorlist: No Uruanyi in mirrorlist
RUN sed -i -e "s|mirrorlist=|#mirrorlist=|g" /etc/yum.repos.d/CentOS-*
RUN sed -i -e "s|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g" /etc/yum.repos.d/CentOS-*

# /www/transcode_linux: error while loading shared libraries: libpng15.so.15: cannot open shared object file: No such file or directory
RUN yum -y install libpng15.x86_64

#下载ffmpeg
#参考 https://computingforgeeks.com/how-to-install-ffmpeg-on-centos-rhel-8/
# https://zhuanlan.zhihu.com/p/120866438
RUN yum -y install yum-utils
RUN dnf -y install epel-release nmap
RUN yum-config-manager --enable powertools
RUN yum-config-manager --add-repo=https://negativo17.org/repos/epel-multimedia.repo

RUN dnf -y install ffmpeg glibc-all-langpacks langpacks-en java-1.8.0-openjdk-devel && dnf clean all

ENV LC_ALL en_US.UTF-8

# Set the JAVA_HOME variable to make it clear where Java is located
ENV JAVA_HOME /usr/lib/jvm/java

RUN rm -f /etc/localtime && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
