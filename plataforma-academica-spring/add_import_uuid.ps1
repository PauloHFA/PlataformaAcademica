$files = Get-ChildItem "src/main/java/com/plataforma_academica/plataforma/repository/*.java"
foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    if ($c -notmatch 'import java\.util\.UUID;') {
        $c = $c -replace '(package com\.plataforma_academica\.plataforma\.repository;)', "`$1`nimport java.util.UUID;"
        [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
        Write-Output "Adicionado import UUID: $($file.Name)"
    }
}
Write-Output "Concluído!"
