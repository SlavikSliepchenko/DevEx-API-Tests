# Makefile for Allure Reports
.PHONY: test report open-report all

test:
	mvn clean test

report:
	mvn allure:report

open-report:
	mvn allure:serve

all:
	mvn clean test allure:report allure:serve