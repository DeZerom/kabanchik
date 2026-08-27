package ru.kabanchik.common.files.api

class FileOpeningException(
    cause: Throwable,
) : Exception("Unable to open file", cause)
