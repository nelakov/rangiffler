CREATE DATABASE IF NOT EXISTS `rangiffler-geo`;
USE `rangiffler-geo`;

CREATE TABLE IF NOT EXISTS country
(
    id           BINARY(16) unique  not null default (UUID_TO_BIN(UUID(), TRUE)),
    country_name varchar(50) unique not null,
    country_code varchar(255)       not null,
    primary key (id)
);

insert into country(country_name, country_code)
values ('Fiji', 'FJ');

insert into country(country_name, country_code)
values ('Tanzania', 'TZ');

insert into country(country_name, country_code)
values ('Western Sahara', 'EH');

insert into country(country_name, country_code)
values ('Canada', 'CA');

insert into country(country_name, country_code)
values ('United States', 'US');

insert into country(country_name, country_code)
values ('Kazakhstan', 'KZ');

insert into country(country_name, country_code)
values ('Uzbekistan', 'UZ');

insert into country(country_name, country_code)
values ('Papua New Guinea', 'PG');

insert into country(country_name, country_code)
values ('Indonesia', 'ID');

insert into country(country_name, country_code)
values ('Argentina', 'AR');

insert into country(country_name, country_code)
values ('Chile', 'CL');

insert into country(country_name, country_code)
values ('Democratic Republic of the Congo', 'CD');

insert into country(country_name, country_code)
values ('Somalia', 'SO');

insert into country(country_name, country_code)
values ('Kenya', 'KE');

insert into country(country_name, country_code)
values ('Sudan', 'SD');

insert into country(country_name, country_code)
values ('Chad', 'TD');

insert into country(country_name, country_code)
values ('Haiti', 'HT');

insert into country(country_name, country_code)
values ('Dominican Republic', 'DO');

insert into country(country_name, country_code)
values ('Russia', 'RU');

insert into country(country_name, country_code)
values ('Bahamas', 'BS');

insert into country(country_name, country_code)
values ('Falkland Islands', 'FK');

insert into country(country_name, country_code)
values ('Norway', 'NO');

insert into country(country_name, country_code)
values ('Greenland', 'GL');

insert into country(country_name, country_code)
values ('Timor-Leste', 'TL');

insert into country(country_name, country_code)
values ('South Africa', 'ZA');

insert into country(country_name, country_code)
values ('Lesotho', 'LS');

insert into country(country_name, country_code)
values ('Mexico', 'MX');

insert into country(country_name, country_code)
values ('Uruguay', 'UY');

insert into country(country_name, country_code)
values ('Brazil', 'BR');

insert into country(country_name, country_code)
values ('Bolivia', 'BO');

insert into country(country_name, country_code)
values ('Peru', 'PE');

insert into country(country_name, country_code)
values ('Colombia', 'CO');

insert into country(country_name, country_code)
values ('Panama', 'PA');

insert into country(country_name, country_code)
values ('Costa Rica', 'CR');

insert into country(country_name, country_code)
values ('Nicaragua', 'NI');

insert into country(country_name, country_code)
values ('Honduras', 'HN');

insert into country(country_name, country_code)
values ('El Salvador', 'SV');

insert into country(country_name, country_code)
values ('Guatemala', 'GT');

insert into country(country_name, country_code)
values ('Belize', 'BZ');

insert into country(country_name, country_code)
values ('Venezuela', 'VE');

insert into country(country_name, country_code)
values ('Guyana', 'GY');

insert into country(country_name, country_code)
values ('Suriname', 'SR');

insert into country(country_name, country_code)
values ('France', 'FR');

insert into country(country_name, country_code)
values ('Ecuador', 'EC');

insert into country(country_name, country_code)
values ('Puerto Rico', 'PR');

insert into country(country_name, country_code)
values ('Jamaica', 'JM');

insert into country(country_name, country_code)
values ('Cuba', 'CU');

insert into country(country_name, country_code)
values ('Zimbabwe', 'ZW');

insert into country(country_name, country_code)
values ('Botswana', 'BW');

insert into country(country_name, country_code)
values ('Namibia', 'NA');

insert into country(country_name, country_code)
values ('Senegal', 'SN');

insert into country(country_name, country_code)
values ('Mali', 'ML');

insert into country(country_name, country_code)
values ('Mauritania', 'MR');

insert into country(country_name, country_code)
values ('Benin', 'BJ');

insert into country(country_name, country_code)
values ('Niger', 'NE');

insert into country(country_name, country_code)
values ('Nigeria', 'NG');

insert into country(country_name, country_code)
values ('Cameroon', 'CM');

insert into country(country_name, country_code)
values ('Togo', 'TG');

insert into country(country_name, country_code)
values ('Ghana', 'GH');

insert into country(country_name, country_code)
values ('Côted''Ivoire', 'CI');

insert into country(country_name, country_code)
values ('Guinea', 'GN');

insert into country(country_name, country_code)
values ('Guinea-Bissau', 'GW');

insert into country(country_name, country_code)
values ('Liberia', 'LR');

insert into country(country_name, country_code)
values ('Sierra Leone', 'SL');

insert into country(country_name, country_code)
values ('Burkina Faso', 'BF');

insert into country(country_name, country_code)
values ('Central African Republic', 'CF');

insert into country(country_name, country_code)
values ('Republic of the Congo', 'CG');

insert into country(country_name, country_code)
values ('Gabon', 'GA');

insert into country(country_name, country_code)
values ('Equatorial Guinea', 'GQ');

insert into country(country_name, country_code)
values ('Zambia', 'ZM');

insert into country(country_name, country_code)
values ('Malawi', 'MW');

insert into country(country_name, country_code)
values ('Mozambique', 'MZ');

insert into country(country_name, country_code)
values ('Eswatini', 'SZ');

insert into country(country_name, country_code)
values ('Angola', 'AO');

insert into country(country_name, country_code)
values ('Burundi', 'BI');

insert into country(country_name, country_code)
values ('Israel', 'IL');

insert into country(country_name, country_code)
values ('Lebanon', 'LB');

insert into country(country_name, country_code)
values ('Madagascar', 'MG');

insert into country(country_name, country_code)
values ('Palestine', 'PS');

insert into country(country_name, country_code)
values ('The Gambia', 'GM');

insert into country(country_name, country_code)
values ('Tunisia', 'TN');

insert into country(country_name, country_code)
values ('Algeria', 'DZ');

insert into country(country_name, country_code)
values ('Jordan', 'JO');

insert into country(country_name, country_code)
values ('United Arab Emirates', 'AE');

insert into country(country_name, country_code)
values ('Qatar', 'QA');

insert into country(country_name, country_code)
values ('Kuwait', 'KW');

insert into country(country_name, country_code)
values ('Iraq', 'IQ');

insert into country(country_name, country_code)
values ('Oman', 'OM');

insert into country(country_name, country_code)
values ('Vanuatu', 'VU');

insert into country(country_name, country_code)
values ('Cambodia', 'KH');

insert into country(country_name, country_code)
values ('Thailand', 'TH');

insert into country(country_name, country_code)
values ('Lao PDR', 'LA');

insert into country(country_name, country_code)
values ('Myanmar', 'MM');

insert into country(country_name, country_code)
values ('Vietnam', 'VN');

insert into country(country_name, country_code)
values ('Dem. Rep. Korea', 'KP');

insert into country(country_name, country_code)
values ('Republic of Korea', 'KR');

insert into country(country_name, country_code)
values ('Mongolia', 'MN');

insert into country(country_name, country_code)
values ('India', 'IN');

insert into country(country_name, country_code)
values ('Bangladesh', 'BD');

insert into country(country_name, country_code)
values ('Bhutan', 'BT');

insert into country(country_name, country_code)
values ('Nepal', 'NP');

insert into country(country_name, country_code)
values ('Pakistan', 'PK');

insert into country(country_name, country_code)
values ('Afghanistan', 'AF');

insert into country(country_name, country_code)
values ('Tajikistan', 'TJ');

insert into country(country_name, country_code)
values ('Kyrgyzstan', 'KG');

insert into country(country_name, country_code)
values ('Turkmenistan', 'TM');

insert into country(country_name, country_code)
values ('Iran', 'IR');

insert into country(country_name, country_code)
values ('Syria', 'SY');

insert into country(country_name, country_code)
values ('Armenia', 'AM');

insert into country(country_name, country_code)
values ('Sweden', 'SE');

insert into country(country_name, country_code)
values ('Belarus', 'BY');

insert into country(country_name, country_code)
values ('Ukraine', 'UA');

insert into country(country_name, country_code)
values ('Poland', 'PL');

insert into country(country_name, country_code)
values ('Austria', 'AT');

insert into country(country_name, country_code)
values ('Hungary', 'HU');

insert into country(country_name, country_code)
values ('Moldova', 'MD');

insert into country(country_name, country_code)
values ('Romania', 'RO');

insert into country(country_name, country_code)
values ('Lithuania', 'LT');

insert into country(country_name, country_code)
values ('Latvia', 'LV');

insert into country(country_name, country_code)
values ('Estonia', 'EE');

insert into country(country_name, country_code)
values ('Germany', 'DE');

insert into country(country_name, country_code)
values ('Bulgaria', 'BG');

insert into country(country_name, country_code)
values ('Greece', 'GR');

insert into country(country_name, country_code)
values ('Turkey', 'TR');

insert into country(country_name, country_code)
values ('Albania', 'AL');

insert into country(country_name, country_code)
values ('Croatia', 'HR');

insert into country(country_name, country_code)
values ('Switzerland', 'CH');

insert into country(country_name, country_code)
values ('Luxembourg', 'LU');

insert into country(country_name, country_code)
values ('Belgium', 'BE');

insert into country(country_name, country_code)
values ('Netherlands', 'NL');

insert into country(country_name, country_code)
values ('Portugal', 'PT');

insert into country(country_name, country_code)
values ('Spain', 'ES');

insert into country(country_name, country_code)
values ('Ireland', 'IE');

insert into country(country_name, country_code)
values ('New Caledonia', 'NC');

insert into country(country_name, country_code)
values ('Solomon Islands', 'SB');

insert into country(country_name, country_code)
values ('New Zealand', 'NZ');

insert into country(country_name, country_code)
values ('Australia', 'AU');

insert into country(country_name, country_code)
values ('Sri Lanka', 'LK');

insert into country(country_name, country_code)
values ('China', 'CN');

insert into country(country_name, country_code)
values ('Taiwan', 'TW');

insert into country(country_name, country_code)
values ('Italy', 'IT');

insert into country(country_name, country_code)
values ('Denmark', 'DK');

insert into country(country_name, country_code)
values ('United Kingdom', 'GB');

insert into country(country_name, country_code)
values ('Iceland', 'IS');

insert into country(country_name, country_code)
values ('Azerbaijan', 'AZ');

insert into country(country_name, country_code)
values ('Georgia', 'GE');

insert into country(country_name, country_code)
values ('Philippines', 'PH');

insert into country(country_name, country_code)
values ('Malaysia', 'MY');

insert into country(country_name, country_code)
values ('Brunei Darussalam', 'BN');

insert into country(country_name, country_code)
values ('Slovenia', 'SI');

insert into country(country_name, country_code)
values ('Finland', 'FI');

insert into country(country_name, country_code)
values ('Slovakia', 'SK');

insert into country(country_name, country_code)
values ('Czech Republic', 'CZ');

insert into country(country_name, country_code)
values ('Eritrea', 'ER');

insert into country(country_name, country_code)
values ('Japan', 'JP');

insert into country(country_name, country_code)
values ('Paraguay', 'PY');

insert into country(country_name, country_code)
values ('Yemen', 'YE');

insert into country(country_name, country_code)
values ('Saudi Arabia', 'SA');

insert into country(country_name, country_code)
values ('Northern Cyprus', 'CYP');

insert into country(country_name, country_code)
values ('Cyprus', 'CY');

insert into country(country_name, country_code)
values ('Morocco', 'MA');

insert into country(country_name, country_code)
values ('Egypt', 'EG');

insert into country(country_name, country_code)
values ('Libya', 'LY');

insert into country(country_name, country_code)
values ('Ethiopia', 'ET');

insert into country(country_name, country_code)
values ('Djibouti', 'DJ');

insert into country(country_name, country_code)
values ('Somaliland', 'SOM');

insert into country(country_name, country_code)
values ('Uganda', 'UG');

insert into country(country_name, country_code)
values ('Rwanda', 'RW');

insert into country(country_name, country_code)
values ('Bosnia and Herzegovina', 'BA');

insert into country(country_name, country_code)
values ('Macedonia', 'MK');

insert into country(country_name, country_code)
values ('Serbia', 'RS');

insert into country(country_name, country_code)
values ('Montenegro', 'ME');

insert into country(country_name, country_code)
values ('Kosovo', 'XK');

insert into country(country_name, country_code)
values ('Trinidad and Tobago', 'TT');

insert into country(country_name, country_code)
values ('South Sudan', 'SS');