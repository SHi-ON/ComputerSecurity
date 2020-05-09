cmake_minimum_required(VERSION 3.14)
project(ComputerSecurity)
set(CMAKE_CXX_STANDARD 11)

add_compile_options("-fstack-protector-strong")

add_executable(genuine bof.c)