# Makefile for Allure Reports

.PHONY: test report open-report all

test:
	mvn clean test

report:
	allure generate target/allure-results --clean -o target/allure-report

open-report:
	allure open target/allure-report

all: test report open-report