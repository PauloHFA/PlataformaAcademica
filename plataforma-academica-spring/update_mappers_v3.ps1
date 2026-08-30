$files = Get-ChildItem "src/main/java/com/plataforma_academica/plataforma/mapper/*.java"
foreach ($file in $files) {
    $c = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::UTF8)
    if ($c -match 'Long') {
        $c = $c.Replace('Long', 'UUID')
        [System.IO.File]::WriteAllText($file.FullName, $c, (New-Object System.Text.UTF8Encoding $false))
        Write-Output "Atualizado Mapper: $($file.Name)"
    }
}
Write-Output "Concluído!"
